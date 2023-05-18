/*Modified EjectionSeat class for the SAS Engine Mod*/

package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.*;

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
        if(bIsSK1){
        	hierMesh().hideSubTrees("Blister1_D0");
        	Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			localWreckage.collide(false);
			Vector3d localVector3d = new Vector3d();
			localVector3d.set(ownerAircraft.FM.Vwld);
			localWreckage.setSpeed(localVector3d);
        }
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft)
    {
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
            
        case 4: // '\004' Martin-Baker Mk5 Seat
            setMesh("3DO/Plane/MB-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
            break;
            
        case 5: // '\004' Martin-Baker Mk7 Seat
            setMesh("3DO/Plane/MB-ESeat/hier_late.him");
            drawing(true);
    		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2F);
    		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
    		Eff3DActor.New(this, findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
    		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2F);
    		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
    		Eff3DActor.New(this, findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
            break;
            
        case 6: // '\004' North American Seat
            setMesh("3DO/Plane/NA-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
            break;
            
        case 7: // '\005' MiG-15/17 KK-1 Seat
            setMesh("3DO/Plane/KK1-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
            break;   
  
        case 8: // '\005' MiG-17/19 KK-2 Seat
            setMesh("3DO/Plane/KK2-ESeat/hier.him");
            drawing(true);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
            break;   
            
        case 9: // '\006' MiG-21 SK-1 Escape Capsule
            setMesh("3DO/Plane/MiG21-SK1-ESeat/hier.him");
            drawing(true);
            bIsSK1 = true;
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1F);
            Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
            break;
         
        case 10: // '\007' MiG-21 KM-1 Seat
            setMesh("3DO/Plane/KM1-ESeat/hier.him");
            drawing(true);
    		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2F);
    		Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
    		Eff3DActor.New(this, findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
    		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2F);
    		Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1F);
    		Eff3DActor.New(this, findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
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
    private boolean bIsSK1 = false;
}