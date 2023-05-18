// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 12/10/2015 03:33:41 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mig_15UTI.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            Mig_15F, PaintSchemeFMPar1956, PaintSchemeFMPar06, Aircraft, 
//            Cockpit, NetAircraft, EjectionSeat

public class Mig_15UTI extends Mig_15F
{

    public Mig_15UTI()
    {
        lTimeNextEject = 0L;
        overrideBailout = false;
        ejectComplete = false;
        super.isTrainer = true;
        lTimeNextEject = 0L;
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            return super.cutFM(34, j, actor);

        case 36: // '$'
            return super.cutFM(37, j, actor);

        case 11: // '\013'
            cutFM(17, j, actor);
            super.FM.cut(17, j, actor);
            cutFM(18, j, actor);
            super.FM.cut(18, j, actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((((FlightModelMain) (super.FM)).Gears.nearGround() || ((FlightModelMain) (super.FM)).Gears.onGround()) && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() != 6)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this) {

            public void doAction(Object paramObject)
            {
                Aircraft localAircraft = (Aircraft)paramObject;
                if(Actor.isValid(localAircraft))
                {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
                    ((Actor) (localAircraft)).pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2, localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).x;
                    localVector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).y;
                    localVector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, localLoc1, localVector3d, localAircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor()
    {
        new MsgAction(false, this) {

            public void doAction(Object paramObject)
            {
                Aircraft localAircraft = (Aircraft)paramObject;
                if(Actor.isValid(localAircraft))
                {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat02");
                    ((Actor) (localAircraft)).pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2, localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).x;
                    localVector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).y;
                    localVector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, localLoc1, localVector3d, localAircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 0 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep < 2)
            {
                if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F && ((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.5F)
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
                    doRemoveBlisters();
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 2;
                }
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 2 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 3)
            {
                switch(((FlightModelMain) (super.FM)).AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(((FlightModelMain) (super.FM)).CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if(((FlightModelMain) (super.FM)).AS.astateBailoutStep == 4)
                    ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 11;
            } else
            if(((FlightModelMain) (super.FM)).AS.astateBailoutStep >= 11 && ((FlightModelMain) (super.FM)).AS.astateBailoutStep <= 19)
            {
                int i = ((FlightModelMain) (super.FM)).AS.astateBailoutStep;
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(20, ((FlightModelMain) (super.FM)).AS.astateBailoutStep, 1, null);
                ((FlightModelMain) (super.FM)).AS.astateBailoutStep = (byte)(((FlightModelMain) (super.FM)).AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(((FlightModelMain) (super.FM)).AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(((FlightModelMain) (super.FM)).AS.astatePilotStates[i - 11] < 99)
                {
                    if(i == 11)
                    {
                        doRemoveBodyFromPlane(2);
                        doEjectCatapultStudent();
                        lTimeNextEject = Time.current() + 1000L;
                    } else
                    if(i == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapultInstructor();
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
                        ((FlightModelMain) (super.FM)).AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    ((FlightModelMain) (super.FM)).AS.astatePilotStates[i - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (i - 11) + "]=" + ((FlightModelMain) (super.FM)).AS.astatePilotStates[i - 11]);
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(((FlightModelMain) (super.FM)).Vwld);
            localWreckage.setSpeed(localVector3d);
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(((FlightModelMain) (super.FM)).Vwld);
                localWreckage.setSpeed(localVector3d);
            }

    }

    public void moveCockpitDoor(float paramFloat)
    {
        resetYPRmodifier();
        if(((FlightModelMain) (super.FM)).AS.astatePlayerIndex == 1)
        {
            if(paramFloat < 0.1F)
                Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.08F, 0.0F, 0.1F);
            else
                Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.17F, 0.99F, 0.1F, 0.4F);
            hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            if(Config.isUSE_RENDER() && Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
            setDoorSnd(paramFloat);
        }
    }

    public void update(float f)
    {
        if((((FlightModelMain) (super.FM)).AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            ((FlightModelMain) (super.FM)).AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        try
        {
            if(((FlightModelMain) (super.FM)).AS.astatePlayerIndex == 1)
                ((FlightModelMain) (super.FM)).CT.setActiveDoor(1);
            else
                ((FlightModelMain) (super.FM)).CT.setActiveDoor(2);
            if(((FlightModelMain) (super.FM)).CT.bMoveSideDoor)
            {
                hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 90F * ((FlightModelMain) (super.FM)).CT.getCockpitDoor(), 0.0F);
                if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[1] != null)
                    Main3D.cur3D().cockpits[1].onDoorMoved(((FlightModelMain) (super.FM)).CT.getCockpitDoor());
                setDoorSnd(((FlightModelMain) (super.FM)).CT.getCockpitDoor());
            }
        }
        catch(Throwable throwable) { }
        super.update(f);
    }

    public static boolean bChangedPit = false;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Mig_15UTI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15");
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_sk", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_ro", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_hu", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15UTI.fmd:UTI");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMig_15F.class,
            com.maddox.il2.objects.air.CockpitMig_15UTIi.class
        });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 0, 0, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02"
        });
        String as[] = new String[5];
        as[1] = "MGunUBki 120";
        Aircraft.weaponsRegister(class1, "default", as);
        Aircraft.weaponsRegister(class1, "2xDroptanks", new String[]
        {
            null, "MGunUBki 120", null, "FTGunL 1", "FTGunR 1"
        });
        Aircraft.weaponsRegister(class1, "none", new String[5]);
    }
}
