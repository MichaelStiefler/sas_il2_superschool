package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Message;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Time;

public class EjectionSeat extends ActorHMesh {
    class Interpolater extends Interpolate {

        public boolean tick() {
            float f = Time.tickLenFs();
            v.z -= 9.81D * (double) f * (double) f;
            v.x *= 0.99D;
            v.y *= 0.99D;
            l.add(v);
            pos.setAbs(l);
            World.cur();
            double d = World.land().HQ_Air(l.getPoint().x, l.getPoint().y);
            if (l.getPoint().z < d)
                MsgDestroy.Post(Time.current(), super.actor);
            if (bPilotAttached && (l.getPoint().z < d || Time.current() > timeStart + 3000L))
                if (!ownerAircraft.isNet() || ownerAircraft.isNetMaster()) {
                    Vector3d vector3d = new Vector3d(v);
                    vector3d.scale(1.0F / Time.tickLenFs());
                    if (Actor.isValid(ownerAircraft)) {
                        new Paratrooper(ownerAircraft, ownerAircraft.getArmy(), 0, l, vector3d);
                        doRemovePilot();
                        bPilotAttached = false;
                        ownerAircraft.FM.AS.astateBailoutStep = 12;
                        EventLog.onBailedOut(ownerAircraft, 0);
                        ownerAircraft.FM.AS.setPilotState(ownerAircraft, 0, 100, false);
                    }
                } else {
                    doRemovePilot();
                    bPilotAttached = false;
                }
            return true;
        }

        Interpolater() {}
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private void doRemovePilot() {
        hierMesh().chunkVisible("Pilot1_D0", false);
        hierMesh().chunkVisible("Head1_D0", false);
        hierMesh().chunkVisible("HMask1_D0", false);
        if (bIsSK1) {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(ownerAircraft.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft) {
        bIsSK1 = false;
        v = new Vector3d();
        l = new Loc();
        bPilotAttached = true;
        switch (i) {
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

            case 4:
                setMesh("3DO/Plane/MB-ESeat/hier.him");
                drawing(true);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case 5:
                setMesh("3DO/Plane/MB-ESeat/hier_late.him");
                drawing(true);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                break;

            case 6:
                setMesh("3DO/Plane/NA-ESeat/hier.him");
                drawing(true);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case 7:
                setMesh("3DO/Plane/KK1-ESeat/hier.him");
                drawing(true);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case 8:
                setMesh("3DO/Plane/KK2-ESeat/hier.him");
                drawing(true);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case 9:
                setMesh("3DO/Plane/MiG21-SK1-ESeat/hier.him");
                drawing(true);
                bIsSK1 = true;
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case 10:
                setMesh("3DO/Plane/KM1-ESeat/hier.him");
                drawing(true);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                break;

            case 11:
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
    private Loc      l;
    private boolean  bPilotAttached;
    private Aircraft ownerAircraft;
    private long     timeStart;
    private boolean  bIsSK1;

}
