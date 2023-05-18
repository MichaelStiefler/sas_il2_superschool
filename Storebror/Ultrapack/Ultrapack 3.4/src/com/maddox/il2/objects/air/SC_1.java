package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class SC_1 extends Scheme2
    implements TypeSailPlane
{
    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float)Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -65F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f)
    {
        if(this != World.getPlayerAircraft())
            if(this.FM.Gears.onGround())
                FM.AS.setCockpitDoor(this, 1);
            else
                FM.AS.setCockpitDoor(this, 0);
        super.update(f);
    }

    protected void moveRudder(float f)
    {
        if(FM.CT.getGear() > 0.98F)
            hierMesh().chunkSetAngles("Rudder1a_D0", 0.0F, -36F * f, 0.0F);
        super.moveRudder(f);
    }

    static 
    {
        Class class1 = SC_1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Seahawk");
        Property.set(class1, "meshName", "3DO/Plane/SC-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SPITFIRE5float.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSC1.class
        });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 3, 9, 9, 3, 3, 
            9, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev04", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalDev03", "_ExternalBomb03"
        });
    }
}
