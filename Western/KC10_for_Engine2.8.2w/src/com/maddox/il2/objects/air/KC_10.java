
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.*;
import com.maddox.il2.game.*;
import com.maddox.rts.*;


public abstract class KC_10 extends DC_10family
{

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasSideDoor = true;
        FM.CT.bHasFormationLights = true;
    }

    public void missionStarting()
    {
        super.missionStarting();
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        boolean bSideDoorOccupy = false;
        if(FM.CT.bHasSideDoor && FM.CT.bMoveSideDoor)
        {
            if(FM.CT.getCockpitDoor() > 0.0F && FM.CT.getCockpitDoor() < 1.0F)
                bSideDoorOccupy = true;
        }
        if(!bSideDoorOccupy)
            FM.CT.setActiveDoor( 1 );     // setActiveDoor <== not SIDE_DOOR
        if (FM.Gears.onGround() && !bSideDoorOccupy)
        {
            if(FM.brakeShoe && FM.CT.getCockpitDoor() > 0.9F)
                hierMesh().chunkVisible("Stair_D0", true);
            else
                hierMesh().chunkVisible("Stair_D0", false);
        }

        formationlights();
    }

    public void update(float f)
    {
        super.update(f);

    }

    private void formationlights()
    {
        int ws = Mission.cur().curCloudsType();
        float we = Mission.cur().curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || (ws > 4 && FM.getAltitude()<we)) && !FM.isPlayers())
        {
            FM.CT.bFormationLights = true;
        }
        if(((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && ws <= 4) || (World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude()>we)) && !FM.isPlayers())
        {
            FM.CT.bFormationLights = false;
        }
        hierMesh().chunkVisible("SSlightCf", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightNs", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightKl", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightWgl", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightWgr", FM.CT.bFormationLights);
    }

    public void msgShot(Shot shot)
    {
        super.msgShot(shot);
        if(FM.AS.astateEngineStates[0] > 2 && FM.AS.astateEngineStates[1] > 2 && FM.AS.astateEngineStates[2] > 2)
            FM.setCapableOfBMP(false, shot.initiator);
    }

    static
    {
        Class class1 = KC_10.class;
        Property.set(class1, "FlightModel", "FlightModels/KC10fake.fmd:KC10fakeFM");
        Property.set(class1, "iconFar_shortClassName", "KC-10");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1981F);
        Property.set(class1, "yearExpired", 2022F);
        Property.set(class1, "LOSElevation", 0.965F);
    }
}
