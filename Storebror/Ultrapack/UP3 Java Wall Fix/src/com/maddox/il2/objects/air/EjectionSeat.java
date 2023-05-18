package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Message;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Time;

public class EjectionSeat extends ActorHMesh
{
    class Interpolater extends Interpolate
    {

        public boolean tick()
        {
            float f = Time.tickLenFs();
            v.z -= 9.81D * f * f;
            v.x *= 0.99D;
            v.y *= 0.99D;
            l.add(v);
            pos.setAbs(l);
            World.cur();
            double d = World.land().HQ_Air(l.getPoint().x, l.getPoint().y);
            if(l.getPoint().z < d)
                MsgDestroy.Post(Time.current(), actor);
            if(bPilotAttached && (l.getPoint().z < d || Time.current() > timeStart + 3000L))
                if(!ownerAircraft.isNet() || ownerAircraft.isNetMaster())
                {
                    Vector3d vector3d = new Vector3d(v);
                    vector3d.scale(1.0F / Time.tickLenFs());
                    if(Actor.isValid(ownerAircraft))
                    {
                        Paratrooper paratrooper = new Paratrooper(ownerAircraft, ownerAircraft.getArmy(), 0, l, vector3d);
                        doRemovePilot();
                        bPilotAttached = false;
                        ownerAircraft.FM.AS.astateBailoutStep = 12;
                        EventLog.onBailedOut(ownerAircraft, 0);
                        ownerAircraft.FM.AS.setPilotState(ownerAircraft, 0, 100, false);
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
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft)
    {
        v = new Vector3d();
        l = new Loc();
        bPilotAttached = true;
        switch(i)
        {
        case 1:
        default:
            setMesh("3DO/Plane/He-162-ESeat/hier.him");
            drawing(true);
            break;

        case 2:
            setMesh("3DO/Plane/Do-335A-0-ESeat/hier.him");
            drawing(true);
            break;

        case 3:
            setMesh("3DO/Plane/Ar-234-ESeat/hier.him");
            drawing(true);
            break;
        }
        l.set(loc);
        v.set(vector3d);
        v.scale(Time.tickConstLenFs());
        pos.setAbs(l);
        interpPut(new Interpolater(), null, Time.current(), null);
        ownerAircraft = aircraft;
        timeStart = Time.current();
    }

    private Vector3d v;
    private Loc l;
    private boolean bPilotAttached;
    private Aircraft ownerAircraft;
    private long timeStart;







}
