
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, PaintScheme

public abstract class SU_26XX extends Scheme1
{

    public SU_26XX()
    {
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            if(!FM.AS.bIsAboutToBailout && World.cur().isHighGore())
            {
                if(hierMesh().isChunkVisible("Blister1_D0"))
                    hierMesh().chunkVisible("Gore1_D0", true);
                hierMesh().chunkVisible("Gore2_D0", true);
            }
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
    }

    protected void moveGear(float f, float f1, float f2)
    {
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("CF"))
        {
            if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
                FM.AS.hitTank(shot.initiator, 0, 1);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
        }
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
            FM.AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Oil") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.9F)
        {
            FM.AS.hitOil(shot.initiator, 0);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if(shot.chunkName.startsWith("Pilot"))
        {
            killPilot(shot.initiator, 0);
            if(Pd.z > 0.64999997615814209D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                HUD.logCenter("H E A D S H O T");
            return;
        } else
        {
            super.msgShot(shot);
            return;
        }
    }

    protected void moveFlap(float f)
    {
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            return super.cutFM(35, j, actor);

        case 36: // '$'
            return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.air.SU_26XX.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
