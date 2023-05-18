// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:11:27
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EjectionSeat.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            Aircraft, Paratrooper

public class EjectionSeat extends ActorHMesh
{
    class Interpolater extends Interpolate
    {

        public boolean tick()
        {
            float f = Time.tickLenFs();
            v.z -= 9.8100000000000005D * (double)f * (double)f;
            v.x *= 0.99000000953674316D;
            v.y *= 0.99000000953674316D;
            l.add(v);
            pos.setAbs(l);
            World.cur();
            double d = World.land().HQ_Air(((Tuple3d) (l.getPoint())).x, ((Tuple3d) (l.getPoint())).y);
            if(((Tuple3d) (l.getPoint())).z < d)
                MsgDestroy.Post(Time.current(), super.actor);
            if(bPilotAttached && (((Tuple3d) (l.getPoint())).z < d || Time.current() > timeStart + 3000L))
                if(!ownerAircraft.isNet() || ownerAircraft.isNetMaster())
                {
                    Vector3d vector3d = new Vector3d(v);
                    vector3d.scale(1.0F / Time.tickLenFs());
                    if(Actor.isValid(ownerAircraft))
                    {
                        new Paratrooper(ownerAircraft, ownerAircraft.getArmy(), 0, l, vector3d);
                        doRemovePilot();
                        bPilotAttached = false;
                        ((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).AS.astateBailoutStep = 12;
                        EventLog.onBailedOut(ownerAircraft, 0);
                        ((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).AS.setPilotState(ownerAircraft, 0, 100, false);
                    }
                } else
                {
                    doRemovePilot();
                    bPilotAttached = false;
                }
            return true;
        }

        Interpolater()
        {
        }
    }


    public Object getSwitchListener(Message message)
    {
        return this;
    }

    private void doRemovePilot()
    {
        hierMesh().chunkVisible("Pilot1_D0", false);
        hierMesh().chunkVisible("Head1_D0", false);
        hierMesh().chunkVisible("HMask1_D0", false);
        if(bIsSK1)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).Vwld);
            localWreckage.setSpeed(localVector3d);
        }
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft)
    {
        bIsSK1 = false;
        v = new Vector3d();
        l = new Loc();
        bPilotAttached = true;
        switch(i)
        {
        case 1: // '\001'
        default:
            setMesh("3DO/Plane/He-162-ESeat/hier.him");
            drawing(true);
            break;

        case 2: // '\002'
            setMesh("3DO/Plane/Do-335A-0-ESeat/hier.him");
            drawing(true);
            break;

        case 3: // '\003'
            setMesh("3DO/Plane/Ar-234-ESeat/hier.him");
            drawing(true);
            break;

        case 4: // '\004'
            setMesh("3DO/Plane/MB-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            break;

        case 5: // '\005'
            setMesh("3DO/Plane/MB-ESeat/hier_late.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
            Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
            Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
            break;

        case 6: // '\006'
            setMesh("3DO/Plane/NA-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            break;

        case 7: // '\007'
            setMesh("3DO/Plane/KK1-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            break;

        case 8: // '\b'
            setMesh("3DO/Plane/KK2-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            break;

        case 9: // '\t'
            setMesh("3DO/Plane/MiG21-SK1-ESeat/hier.him");
            drawing(true);
            bIsSK1 = true;
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            break;

        case 10: // '\n'
            setMesh("3DO/Plane/KM1-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
            Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
            Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
            Eff3DActor.New(this, findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
            break;

        case 11: // '\013'
            setMesh("3DO/Plane/MeteorF8-ESeat/hier.him");
            drawing(true);
            break;
        }
        l.set(loc);
        v.set(vector3d);
        v.scale(Time.tickConstLenFs());
        super.pos.setAbs(l);
        interpPut(new Interpolater(), null, Time.current(), null);
        ownerAircraft = aircraft;
        timeStart = Time.current();
    }

    private Vector3d v;
    private Loc l;
    private boolean bPilotAttached;
    private Aircraft ownerAircraft;
    private long timeStart;
    private boolean bIsSK1;







}