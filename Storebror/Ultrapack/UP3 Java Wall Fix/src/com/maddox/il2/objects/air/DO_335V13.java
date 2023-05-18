package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class DO_335V13 extends DO_335
{

    public DO_335V13()
    {
        bKeelUp = true;
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Water1_D0", 0.0F, -20F * FM.EI.engines[1].getControlRadiator(), 0.0F);
        hierMesh().chunkSetAngles("Water2_D0", 0.0F, -10F * FM.EI.engines[1].getControlRadiator(), 0.0F);
        hierMesh().chunkSetAngles("Water3_D0", 0.0F, -10F * FM.EI.engines[1].getControlRadiator(), 0.0F);
        for(int i = 2; i < 8; i++)
            hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -30F * FM.EI.engines[0].getControlRadiator(), 0.0F);

        super.update(f);
        if(FM.AS.isMaster() && bKeelUp && FM.AS.astateBailoutStep == 3)
        {
            FM.AS.setInternalDamage(this, 5);
            FM.AS.setInternalDamage(this, 4);
            bKeelUp = false;
        }
    }

    public final void doKeelShutoff()
    {
        nextDMGLevels(4, 2, "Keel1_D" + chunkDamageVisible("Keel1"), this);
        oldProp[1] = 99;
        Wreckage wreckage;
        if(hierMesh().isChunkVisible("Prop2_D1"))
            wreckage = new Wreckage(this, hierMesh().chunkFind("Prop2_D1"));
        else
            wreckage = new Wreckage(this, hierMesh().chunkFind("Prop2_D0"));
        Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE, 3F);
        Vector3d vector3d = new Vector3d();
        vector3d.set(FM.Vwld);
        wreckage.setSpeed(vector3d);
        hierMesh().chunkVisible("Prop2_D0", false);
        hierMesh().chunkVisible("Prop2_D1", false);
        hierMesh().chunkVisible("PropRot2_D0", false);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xxcannon04"))
        {
            debuggunnery("Armament System: Left Wing Cannon: Disabled..");
            FM.AS.setJamBullets(1, 1);
            getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
            return;
        }
        if(s.startsWith("xxcannon05"))
        {
            debuggunnery("Armament System: Right Wing Cannon: Disabled..");
            FM.AS.setJamBullets(1, 2);
            getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
            return;
        } else
        {
            super.hitBone(s, shot, point3d);
            return;
        }
    }

    private boolean bKeelUp;

    static 
    {
        Class class1 = DO_335V13.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Do-335");
        Property.set(class1, "meshName", "3DO/Plane/Do-335V-13/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Do-335V-13.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDO_335V13.class });
        Property.set(class1, "LOSElevation", 1.00705F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 1, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN02", "_MGUN03", "_MGUN01", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn01", "_BombSpawn03", "_BombSpawn01", 
            "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11"
        });
    }
}
