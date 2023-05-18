// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 16-Nov-12 10:36:55 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   OrderGT.java

package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

// Referenced classes of package com.maddox.il2.game.order:
//            Order, OrdersTree

class OrderGT extends Order
{

    public OrderGT(String s)
    {
        super(s);
        Pd = new Point3d();
    }

    public void run(int i)
    {
        Voice.setSyncMode(1);
        for(int j = 0; j < CommandSet().length; j++)
        {
            Aircraft aircraft = CommandSet()[j];
            if(Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor))
            {
                Pilot pilot = (Pilot)aircraft.FM;
                pilot.attackGround(i);
                boolean flag = false;
                if(pilot.Group != null)
                {
                    Pd.set(pilot.Group.Pos);
                    if(OrdersTree.curOrdersTree.alone() && (pilot.Group.grTask != 4 || pilot.Group.gTargetPreference != i) && ((Maneuver)Player().FM).Group == pilot.Group)
                    {
                        AirGroup airgroup = new AirGroup(pilot.Group);
                        pilot.Group.delAircraft(aircraft);
                        airgroup.addAircraft(aircraft);
                    }
                    pilot.Group.setGTargMode(i);
                    pilot.Group.setGTargMode(Pd, 8000F);
                    Actor actor = pilot.Group.setGAttackObject(pilot.Group.numInGroup(aircraft));
                    if(actor != null)
                    {
                        if(isEnableVoice() && CommandSet()[j] != Player())
                            if(CommandSet()[j].getWing() == Player().getWing() || CommandSet()[j].aircIndex() == 0)
                                Voice.speakAttackGround(CommandSet()[j]);
                            else
                                Voice.speakOk(CommandSet()[j]);
                        pilot.target_ground = null;
                        pilot.Group.setGroupTask(4);
                        flag = true;
                    }
                }
                if(isEnableVoice() && CommandSet()[j] != Player() && !flag)
                    Voice.speakUnable(CommandSet()[j]);
            }
        }

        Voice.setSyncMode(0);
    }

    private Point3d Pd;
}