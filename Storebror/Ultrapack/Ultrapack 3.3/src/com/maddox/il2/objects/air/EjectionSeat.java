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

public class EjectionSeat extends ActorHMesh {
    class Interpolater extends Interpolate {

        public boolean tick() {
            float f = Time.tickLenFs();
            EjectionSeat.this.v.z -= 9.81D * f * f;
            EjectionSeat.this.v.x *= 0.99D;
            EjectionSeat.this.v.y *= 0.99D;
            EjectionSeat.this.l.add(EjectionSeat.this.v);
            EjectionSeat.this.pos.setAbs(EjectionSeat.this.l);
            World.cur();
            double d = World.land().HQ_Air(EjectionSeat.this.l.getPoint().x, EjectionSeat.this.l.getPoint().y);
            if (EjectionSeat.this.l.getPoint().z < d) MsgDestroy.Post(Time.current(), this.actor);
            if (EjectionSeat.this.bPilotAttached && (EjectionSeat.this.l.getPoint().z < d || Time.current() > EjectionSeat.this.timeStart + 3000L)) if (!EjectionSeat.this.ownerAircraft.isNet() || EjectionSeat.this.ownerAircraft.isNetMaster()) {
                Vector3d vector3d = new Vector3d(EjectionSeat.this.v);
                vector3d.scale(1.0F / Time.tickLenFs());
                if (Actor.isValid(EjectionSeat.this.ownerAircraft)) {
                    new Paratrooper(EjectionSeat.this.ownerAircraft, EjectionSeat.this.ownerAircraft.getArmy(), 0, EjectionSeat.this.l, vector3d);
                    EjectionSeat.this.doRemovePilot();
                    EjectionSeat.this.bPilotAttached = false;
                    EjectionSeat.this.ownerAircraft.FM.AS.astateBailoutStep = 12;
                    EventLog.onBailedOut(EjectionSeat.this.ownerAircraft, 0);
                    EjectionSeat.this.ownerAircraft.FM.AS.setPilotState(EjectionSeat.this.ownerAircraft, 0, 100, false);
                }
            } else {
                EjectionSeat.this.doRemovePilot();
                EjectionSeat.this.bPilotAttached = false;
            }
            return true;
        }

        Interpolater() {
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private void doRemovePilot() {
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("HMask1_D0", false);
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft) {
        this.v = new Vector3d();
        this.l = new Loc();
        this.bPilotAttached = true;
        switch (i) {
            case 1:
            default:
                this.setMesh("3DO/Plane/He-162-ESeat/hier.him");
                this.drawing(true);
                break;

            case 2:
                this.setMesh("3DO/Plane/Do-335A-0-ESeat/hier.him");
                this.drawing(true);
                break;

            case 3:
                this.setMesh("3DO/Plane/Ar-234-ESeat/hier.him");
                this.drawing(true);
                break;
        }
        this.l.set(loc);
        this.v.set(vector3d);
        this.v.scale(Time.tickConstLenFs());
        this.pos.setAbs(this.l);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.ownerAircraft = aircraft;
        this.timeStart = Time.current();
    }

    private Vector3d v;
    private Loc      l;
    private boolean  bPilotAttached;
    private Aircraft ownerAircraft;
    private long     timeStart;

}
