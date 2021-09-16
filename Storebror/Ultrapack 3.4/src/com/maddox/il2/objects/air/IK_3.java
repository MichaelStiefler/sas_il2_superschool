package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class IK_3 extends IK_3xyz
    implements TypeFighter
{
    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d)
    {
        super.setOnGround(point3d, orient, vector3d);
        if(this.FM.isPlayers())
        {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 0.5F;
            this.FM.CT.cockpitDoorControl = 1.0F;
        }
    }

    public void update(float f)
    {
        super.update(f);
        if(!this.FM.isPlayers() && this.FM.Gears.onGround())
            if(this.FM.EI.engines[0].getRPM() < 100F)
                this.FM.CT.cockpitDoorControl = 1.0F;
            else
                this.FM.CT.cockpitDoorControl = 0.0F;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 0.65F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 0.0F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    static 
    {
        Class class1 = IK_3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IK-3");
        Property.set(class1, "meshName", "3DO/Plane/IK-3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/IK-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitIK3.class
        });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01"
        });
    }
}
